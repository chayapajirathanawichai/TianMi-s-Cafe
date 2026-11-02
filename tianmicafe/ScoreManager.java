/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;

public class ScoreManager extends BaseScoreManager{

    public ScoreManager(int initialMoney) {
        super(initialMoney);
    }
    
    public void resetScore() {
        this.scoreMoney = 0;
    }
    
    @Override
    public int processServeSuccess(int currentPatience,int basePrice){
        int earned = basePrice;
        if (currentPatience > 60) {
            earned += 10;
            SoundManager.playSound("bonus");
        } else {
            SoundManager.playSound("pop");
        }
        this.scoreMoney += earned;
        return earned;
    }
    
    public void processWrongServePenalty(int penaltyAmount){
        scoreMoney-=penaltyAmount;
        if(scoreMoney<0) scoreMoney=0;
        System.out.println("Wrong Item! Penalty -$"+penaltyAmount);
    }
    public void processCustomerWalkoutPenalty(int penaltyAmount){
        scoreMoney-=penaltyAmount;
        if(scoreMoney<0) scoreMoney=0;
        System.out.println("Customer Walked Out! Penalty -$"+penaltyAmount);
    }
    public int getScoreMoney(){
        return scoreMoney;
    }
}
