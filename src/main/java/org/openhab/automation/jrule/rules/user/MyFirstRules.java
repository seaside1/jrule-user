package org.openhab.automation.jrule.rules.user;

import org.openhab.automation.jrule.items.generated._MyPower1;
import org.openhab.automation.jrule.rules.JRuleEvent;
import org.openhab.automation.jrule.rules.JRuleName;
import org.openhab.automation.jrule.rules.JRuleWhen;

public class MyFirstRules extends JRuleUser {

    
    @JRuleName("testRuleZwave")
    @JRuleWhen(item = _MyPower1.ITEM, trigger = _MyPower1.TRIGGER_CHANGED)
    public synchronized void myFirstRule(JRuleEvent event) {
        logInfo("MyPower1 Changed! from: {} to: {}", event.getOldState(), event.getState());
    }

}
