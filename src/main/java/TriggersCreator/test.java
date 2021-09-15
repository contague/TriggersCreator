package TriggersCreator;

import TriggersCreator.TriggersBody.InsertTriggerCreator;
import TriggersCreator.TriggersBody.UpdateTriggerCreator;
import TriggersCreator.TriggersBody.UpdateTriggerFunctionCreator;

public class test {
    public static void main(String[] args) {
//        AddTriggerFunction addTriggerFunction = new AddTriggerFunction();
//        System.out.println(addTriggerFunction.createFunction("ART_IB".toLowerCase(), "ID_ART_IB".toLowerCase()));
//        InsertTriggerCreator insertTriggerCreator = new InsertTriggerCreator();
//        System.out.println(insertTriggerCreator.createTrigger("ART_IB".toLowerCase()));
        UpdateTriggerCreator updateTriggerCreator = new UpdateTriggerCreator();
        System.out.println(updateTriggerCreator.createTrigger("ART_IB".toLowerCase()));
//        UpdateTriggerFunctionCreator updateTriggerFunctionCreator = new UpdateTriggerFunctionCreator();
//        System.out.println(updateTriggerFunctionCreator.createFunction("ART_IB".toLowerCase(), "ID_ART_IB".toLowerCase()));
    }
}
