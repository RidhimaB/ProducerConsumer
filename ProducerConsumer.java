import java.util.concurrent.ArrayBlockingQueue;

/**
 * Solving the producer and consumer problem in Java. producer adds items to a fixed-length queue.  Multiple
 * consumers remove items from the queue. *
 * @author Ridhima
 * @version 1.0 04/04/2016
 
 */
public class ProducerConsumer {
	
	
	
	
	public static void main(String[] args) {
		
		/*
		 * Declaring Max BufferSize,Minimum random waiting Period,Max random waiting Period,Program Termination Time, Total generates integers
		 */
		
		int bufferSize=10;
	    int waitingPeriodMin=10;
	    int waitingPeriodMax=100;
	    int max_terminationTime=15;
	    int max_generates_integers=14;
	    String logPath = "C:";
	    /*
	     * Create fixed-length queue.
	     */
        ArrayBlockingQueue<Integer> buffer = new ArrayBlockingQueue<Integer>(bufferSize);
        /*
         * Creating Producer and Consumer threads and passing 
         * Max BufferSize,Minimum waiting Period,Max waiting Period,Program Termination Time, Total generates integers as arguments
         */
        Thread prodThread = new Thread(new MyProducer(buffer,bufferSize,waitingPeriodMin,waitingPeriodMax,max_terminationTime,max_generates_integers,logPath+"/producer.txt"), "Producer");
        Thread consThread = new Thread(new MyConsumer(buffer,bufferSize,waitingPeriodMin,waitingPeriodMax,max_terminationTime,logPath+"/consumer.txt"), "Consumer");
        
        /*
         * Starting producer and consumer threads
         */
        prodThread.start();
        consThread.start();
	}

}
