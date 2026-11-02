/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;

public abstract class BaseScoreManager {
    protected int scoreMoney;
    
    public BaseScoreManager(int initialMoney) {
        this.scoreMoney = initialMoney;
    }
    
    public abstract int processServeSuccess(int currentPatience, int basePrice);
    public abstract void processWrongServePenalty(int penaltyAmount);
    public abstract void processCustomerWalkoutPenalty(int penaltyAmount);

    public int getScoreMoney() {
        return scoreMoney;
    }
}

