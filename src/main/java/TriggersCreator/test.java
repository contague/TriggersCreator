package TriggersCreator;

import TriggersCreator.TriggersBody.InsertTriggerCreator;

public class test {
    public static void main(String[] args) {
        InsertTriggerCreator insertTriggerCreator = new InsertTriggerCreator();
        System.out.println(insertTriggerCreator.createTrigger("ART_IB".toLowerCase()));
    }
}
