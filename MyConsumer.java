import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creating MyConsumer.It consumes items from fixed-length queue
 * @author Ridhima
 * @version 1.0 04/04/2016
 
 */
public class MyConsumer implements Runnable{
	
	/**
	 * Declaring variables
	 */
	  private final BlockingQueue<Integer> sharedBuffer;
	  private Random random;
	  private Integer bufferSize; 
	  private Integer waitingPeriodMin;
	  private Integer waitingPeriodMax;
	  private Integer terminationTime;
	  private static PrintStream outStream;    
		/**
		 * Declaring constructor and Initialization of   Max BufferSize,Minimum random waiting Period,Max random waiting Period,Program Termination Time.
		 * 
		 * @param sharedBuffer BlockingQueue
		 * @param bufferSize  Max BufferSize
		 * @param waitingPeriodMin  Minimum random waiting Period
		 * @param waitingPeriodMax Max random waiting Period
		 * @param terminationTime  Program Termination Time
 		 * 
		 */
	  MyConsumer(BlockingQueue<Integer> sharedBuffer,Integer bufferSize,Integer waitingPeriodMin,Integer waitingPeriodMax,Integer terminationTime,String fileName){
		this.sharedBuffer = sharedBuffer;
		this.bufferSize=bufferSize;
		this.waitingPeriodMin=waitingPeriodMin;
		this.waitingPeriodMax= waitingPeriodMax;
		this.terminationTime=terminationTime;
		random = new Random();
		try{
			 File outFile  = new File(fileName);
			 outStream = new PrintStream(new FileOutputStream(outFile));
			}catch(Exception e){
				
			}
	   }
	
	  @Override
	    public void run() {
		  
		  // Declaration 
		  
	    	boolean terminateFlag=true;
	    	
	      // Calculate start and end times	
	    	
	    	long start = System.currentTimeMillis();
	    	long end = start + terminationTime*1000;
	    	int index = 0;
	    	
    	  // Check termination times till reach 15 sec	
	        while (terminateFlag) {
	            try {
	            	
	            	if(index==bufferSize)
	            		index = 0;
	      // Get Random time and make sleep to certain random time
	            	
	            	Thread.sleep(getRandomTime(waitingPeriodMin,waitingPeriodMax));
	            	  String msg = System.currentTimeMillis()+" Millis, Consuming "+consumeElement(0)+" from the buffer location "+index;
	          //      System.out.println(msg);
	                writeLn(msg);
	                
	                index = index + 1 ;
	             
	                
	            } catch (InterruptedException ex) {
	                Logger.getLogger(MyConsumer.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            
	           	long currentTime = System.currentTimeMillis();
	            
	         // Terminate process if it reach 15 sec
	           	
	            if(currentTime>=end){
	            	terminateFlag=false;
	            	long proceedTime = (System.currentTimeMillis()-start)/1000;
	            	String msg="Total Processed Time :" + proceedTime + " Sec";
	            	 System.out.println(msg);
		             writeLn(msg);
	             	if(outStream!=null){
						outStream.flush();
						outStream.close();
					}
	            	System.exit(0);
	            }

	        }
	    }

	    private int consumeElement(int index) throws InterruptedException {
	        // Check if Buffer has items. If empty, then wait() for buffer to get filled
	        while (sharedBuffer.isEmpty()) {
	            synchronized (sharedBuffer) {
	            	String msg="Queue is empty " + Thread.currentThread().getName()
                            + " is waiting , size: " + sharedBuffer.size();
	            	
	          //  	 System.out.println(msg);
		             writeLn(msg);

	                sharedBuffer.wait();
	                
	            }
	        }

	        // Consume data from Buffer and Notify Producer that Data has been consumed from Buffer
	        synchronized (sharedBuffer) {
	        	sharedBuffer.notifyAll();
	        	
	              return (Integer) sharedBuffer.poll();
	        }
	    }
	    
	    private Integer getRandomTime(Integer min,Integer max){
	    	return random.nextInt((max - min) + 1) + min;
		 }
	    public void writeLn(String msg){
			if(outStream!=null){
				outStream.println(msg);
			}
		}
}
