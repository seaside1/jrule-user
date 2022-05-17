package org.openhab.automation.jrule.rules.user;

import org.openhab.automation.jrule.rules.JRule;


public abstract class JRuleUser extends JRule {

    private static final int startDay = 8;
    private static final int endDay = 21;
   
    protected double getDefaultValue(Double state, double defaultValue) {
        return state == null ? defaultValue : state;
    }

    protected boolean systemIsStarted() {
       //Dummy impl since this is not implemented
        return true;
    }
   
    protected boolean timeIsOkforDisturbance() {
        return nowHour() >= startDay && nowHour() <= endDay;
    }   
}
