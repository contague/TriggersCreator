package TriggersCreator;

import TriggersCreator.TriggersBody.AddTrigger;
import TriggersCreator.TriggersBody.AddTriggerFunction;

public class test {
    public static void main(String[] args) {
        AddTriggerFunction addTriggerFunction = new AddTriggerFunction();
        System.out.println(addTriggerFunction.createFunction("ART_IB".toLowerCase(), "ID_ART_IB".toLowerCase()));
//        AddTrigger addTrigger = new AddTrigger();
//        System.out.println(addTrigger.createTrigger("ART_IB".toLowerCase()));
    }
}
