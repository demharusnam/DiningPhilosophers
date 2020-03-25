/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semaphore;

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
    
    /**
     * Update how many philosophers have successfully completed dinner.
     * If all philosophers have successfully completed dinner,
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
    static void updateEatenStatus() {
        printStatements++;
        if (printStatements % 4 == 0) {
            System.out.println("Till now num of philosophers completed dinner is " + eaten);
        }
    }
    
    static class Fork {
        private final Semaphore mutex = new Semaphore(1); // control access to limited resource to avoid deadlock
        private final int id;
        
        Fork(int id) {
            this.id = id + 1;
        }
        
        /**
         * Acquire fork.
         */
        void acquire() {
            mutex.down();
        }
        
        /**
         * Release fork.
         */
        void release() {
            mutex.up();
        }
        
        boolean isAvailable() {
            return mutex.getValue() != 0;
        }
        
        int getID() {
            return this.id;
        }
    }
    
    static class Philosopher extends Thread {
        private final int id;
        private final Fork leftFork;
        private final Fork rightFork;
        
        Philosopher(int id, Fork leftFork, Fork rightFork) {
            this.id = id + 1;
            this.leftFork = leftFork;
            this.rightFork = rightFork;
        }
        
        /**
         * Eat. Philosopher eats for set time.
         */
        void eat() {
            try {
                Thread.sleep(500);
                System.out.println("Philosopher " + this.id + " completed their dinner");
                updateEaten();
                DiningPhilosophers.updateEatenStatus();
            } catch (InterruptedException ex) {
                Logger.getLogger(DiningPhilosophers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Acquire Fork. Check for availability and acquire if possible. Updates
         * are sent to command line.
         * @param fork: Fork object attempting to acquire
         */
        void tryAcquireFork(Fork fork) {
            if (!fork.isAvailable()) {
                System.out.println("Philosopher " + this.id + " is waiting for Fork " + fork.getID());
                DiningPhilosophers.updateEatenStatus();
            }
            fork.acquire();
            System.out.println("Fork " + fork.getID() + " taken by Philosopher " + this.id);
            DiningPhilosophers.updateEatenStatus();
        }
        
        @Override
        public void run() {
            try {
                // put threads to sleep for random activation
                Thread.sleep(new Random().nextInt(500));
                // try to acquire fork
                this.tryAcquireFork(this.leftFork);
                this.tryAcquireFork(this.rightFork);
                // once both forks have been acquired, eat then release forks
                this.eat();
                System.out.println("Philosopher " + this.id + " released fork " + this.leftFork.getID() + " and fork " + this.rightFork.getID());
                this.leftFork.release();
                this.rightFork.release();
                DiningPhilosophers.updateEatenStatus();
            } catch (InterruptedException ex) {
                Logger.getLogger(DiningPhilosophers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        // init forks
        for (int i = 0; i < N; i++) {
            forks[i] = new Fork(i);
        }
        // init philosophers with appropriate forks
        for (int i = 0; i < N; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % N]);
            philosophers[i].start();
        }
    }

}