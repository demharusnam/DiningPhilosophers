/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Default;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
/**
 *
 * @author Mansur Ahmed
 */

public class DiningPhilosophers {
    private static int eaten = 0; // number of philosophers finished eating
    private static int printStatements = 0; // number of statements printed
    private static final int N = 5; // number of total philosophers
    private static Philosopher[] philosophers = new Philosopher[N];
    private static Fork[] forks = new Fork[N];
    // indicates availability of forks to avoid deadlock
    private static final boolean[] forksAvailable = {true, true, true, true, true}; 
    
    
    /**
     * Update how many philosophers have successfully completed dinner. Increment
     * eaten variable. If all philosophers have successfully completed dinner,
     * exit program.
     */
    static void updateEaten() {
        eaten++;
        if (eaten == N) {
            System.out.println("All 5 philosophers have successfully completed dinner!");
            System.exit(0);
        }
    }
    
    /**
     * Check how many have philosophers have eaten. Update status to command line
     * after every 4 print statements have been used.
     */
    static void checkEatenStatus() {
        printStatements++;
        if (printStatements % 4 == 0) {
            System.out.println("Till now num of philosophers completed dinner are " + eaten);
        }
    }
    
    static class Fork {
        private final int id;
        
        Fork(int id) {
            this.id = id + 1;
        }
        
        void acquire() {
            forksAvailable[this.id-1] = false;
        }

        void release() {
            forksAvailable[this.id-1] = true;
        }
        
        int getID() {
            return this.id;
        }
        
        boolean isAvailable() {
            return forksAvailable[this.id-1];
        }
        
    }
    
    static class Philosopher extends Thread {
        private final int id;
        private final Fork leftFork;
        private final Fork rightFork;
        private int timesWaitingInQueue = 0;
        private boolean leftForkIsAcquired = false;
        private boolean rightForkIsAcquired = false; 
        // control variables for declaring waiting status ONLY once
        private boolean waitingForLeftFork = false;
        private boolean waitingForRightFork = true;
        
        Philosopher(int id, Fork leftFork, Fork rightFork) {
            this.id = id + 1;
            this.leftFork = leftFork;
            this.rightFork = rightFork;
            //System.out.println("Philosopher " + this.id + "'s left fork is " + this.leftFork.id + " and right fork is " + this.rightFork.id);
        }
        
        /**
         * Which fork has been acquired. Check and return if param fork has been
         * acquired.
         * @param fork
         * @return 
         */
        boolean forkIsAcquired(Fork fork) {
            return (fork.getID() == this.id) ? this.leftForkIsAcquired : this.rightForkIsAcquired;
        }
        
        /**
         * Are both forks acquired. Check and return if both forks have been
         * acquired.
         * @return 
         */
        boolean areForksAcquired() {
            return leftForkIsAcquired && rightForkIsAcquired;
        }
        
        /**
         * Philosopher eats. Philosopher eats for set amount of time.
         */
        void eat() {
            try {
                Thread.sleep(500);
                System.out.println("Philosopher " + this.id + " completed their dinner");
                updateEaten();
                checkEatenStatus();
            } catch (InterruptedException ex) {
                Logger.getLogger(DiningPhilosophers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Update waiting time for fork. Increment timesWaitingInQueue counter, 
         * and release held fork (if held) to allow other philosopher to finish
         * dining.
         * @param fork 
         */
        void updateTimesWaitingInQueue(Fork fork) {
            // update counter
            this.timesWaitingInQueue++;         
            if (this.timesWaitingInQueue == 12 && forkIsAcquired(fork)) {
                fork.release();
                this.timesWaitingInQueue = 0;
                System.out.println("Philosopher " + this.id + " released fork " + fork.getID());
            }
        }
        
        /**
        * Acquire Fork. Once fork is available, it is acquired and updates
        * are sent to command line.
        */
        void acquireFork(Fork fork) {
            System.out.println("Fork " + fork.getID() + " taken by Philosopher " + this.id);
            fork.acquire();
            if (fork.getID() == this.id) {
                leftForkIsAcquired = true;
            } else {
                rightForkIsAcquired = true;
            }
            DiningPhilosophers.checkEatenStatus();
        }
        
        /**
         * Determine fork availability. If fork is available, acquire it. If it
         * has been acquired, update waiting time in queue. Else, wait for fork.
         * @param fork 
         */
        void forkAvailability(Fork fork) {
            if (fork.isAvailable()) acquireFork(fork);
            else if (forkIsAcquired(fork)) updateTimesWaitingInQueue(fork);
            else {
                if (fork.getID() == this.id && !this.waitingForLeftFork) {
                    System.out.println("Philosopher " + this.id + " is waiting for fork " + fork.getID());
                    DiningPhilosophers.checkEatenStatus();
                    this.waitingForLeftFork = true;
                } else if (!this.waitingForRightFork) {
                    System.out.println("Philosopher " + this.id + " is waiting for fork " + fork.getID());
                    DiningPhilosophers.checkEatenStatus();
                    this.waitingForRightFork = true;
                }
                updateTimesWaitingInQueue(fork);
            }
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    // put threads to sleep for random activation
                    Thread.sleep(new Random().nextInt(500));
                    // check left fork availability & acquire if possible
                    forkAvailability(this.leftFork);
                    forkAvailability(this.rightFork);
                    // eat once both forks have been acquired
                    if (this.areForksAcquired()) {
                        this.eat();
                        this.leftFork.release();
                        this.rightFork.release();
                        this.leftForkIsAcquired = false;
                        this.rightForkIsAcquired = false;
                        System.out.println("Philosopher " + this.id + " released fork " + this.leftFork.getID() + " and fork " + this.rightFork.getID());
                        checkEatenStatus();
                        break;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(DiningPhilosophers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < N; i++) {
            forks[i] = new Fork(i);
        }

        for (int i = 0; i < N; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % N]);
            philosophers[i].start();
        }
    }

}