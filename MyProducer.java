import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creating MyProducer.It adds items to a fixed-length queue
 * @author Ridhima
 * @version 1.0 04/04/2016
 
 */
public class MyProducer implements Runnable
{
	/**
	 * Declaring variables
	 */
	  private final BlockingQueue<Integer> sharedBuffer;
	  private Random random;
	  private Integer bufferSize;
	  private Integer waitingPeriodMin;
	  private Integer waitingPeriodMax; 
	  private Integer terminationTime;
	  private Integer noGenIntegers;
	  private static PrintStream outStream;  
	   
		/**
		 * Declaring constructor and Initialization of Max BufferSize,Minimum random waiting Period,Max random waiting Period,Program Termination Time, Total generates integers.
		 * 
		 * @param sharedBuffer BlockingQueue
		 * @param bufferSize  Max BufferSize
		 * @param waitingPeriodMin  Minimum random waiting Period
		 * @param waitingPeriodMax Max random waiting Period
		 * @param terminationTime  Program Termination Time
		 * @param noGenIntegers  Total generates integer
		 */
	  
	  MyProducer(BlockingQueue<Integer> sharedBuffer,Integer bufferSize,Integer waitingPeriodMin,Integer waitingPeriodMax,Integer terminationTime,Integer noGenIntegers,String fileName){
		this.sharedBuffer = sharedBuffer;
		this.bufferSize=bufferSize;
		this.waitingPeriodMin=waitingPeriodMin;
		this.waitingPeriodMax= waitingPeriodMax;
		this.terminationTime=terminationTime;
		this.noGenIntegers=noGenIntegers;
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
	    	System.out.println("Process Starting .."+ start);
	    	int index = 0;
	    	
	    // Check termination times till reach 15 sec
	    	
	    	while(terminateFlag){
	    		
		        for (int i = 0; i <= noGenIntegers; i++) {	        	
		           
		            try {
		            	if(index==10)
		            		index = 0;
		              boolean res =   produceElement(index , i);
		              
		            if(res){	
		            	String msg =System.currentTimeMillis()+" Millis, Placing "+i+" in the buffer location "+index;
		        //       System.out.println(msg); 
		               writeLn(msg);
		              index = index + 1 ;
		            }else
		            	i = i-1;
		  // Get Random time and make sleep to certain random time  
		            Thread.sleep(getRandomTime(waitingPeriodMin,waitingPeriodMax));
		            } catch (InterruptedException ex) {
		                Logger.getLogger(MyProducer.class.getName()).log(Level.SEVERE, null, ex);
		            }
		           
		        }
		        if(System.currentTimeMillis()>=end){
		        	terminateFlag=false;
		        	if(outStream!=null){
						outStream.flush();
						outStream.close();
					}
		        }
	    	}
	    }

	    private boolean produceElement(int index,Integer element) throws InterruptedException {

	    	 
	        // Check if Buffer is full or not. If full, then wait() for buffer items to get consumed.
	    	
	        while (sharedBuffer.size() == bufferSize) {
	            synchronized (sharedBuffer) {
	            	String msg ="Queue is full " + Thread.currentThread().getName()
                            + " is waiting , size: " + sharedBuffer.size();
	            //    System.out.println(msg);
	                writeLn(msg);
	                sharedBuffer.wait();
	                return false;
	            }
	        }

	        // Generate data and put it into Buffer and Notify Consumer that Data has been placed into Buffer.
	        synchronized (sharedBuffer) {
	        	sharedBuffer.put(element);
	        	sharedBuffer.notifyAll();
	            return true;
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
