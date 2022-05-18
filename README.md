# jrule-user

The purpose of this repo is to provide a template for developing rule using https://github.com/seaside1/jrule (Java Rules in OpenHAB).
The output will be a jar containing the user defined rules. The jar-file can be added to JRule in OpenHAB
Requirements
- Java 11
- Maven 3.x+

1. Clone repo git clone https://github.com/seaside1/jrule-user/
2. Run maven to build: maven clean install
3. Import project or build in your preferred IDEA (Intellij, Eclipse etc)
4. Start developing and adding rules to your org.openhab.automation.jrule.rules.user package
5. Build with mvn install and deploy it in OpenHAB by placing the created jar-file (jrule-user-1.0-SNAPSHOT.jar) under:  /etc/automation/jrule/rules-jar/

The project is depending on some jars:

1. jrule-items.jar (This file is containing some sample items see https://github.com/seaside1/jrule-user/blob/main/items/jrule-user.items
2. jrule.jar (Dependency on latest snapshot of https://github.com/seaside1/jrule
3. junit-jupiter (For JUnit 5)
4. Mockito (For mocking rules)
5. Variouse OpenHAB core dependencies

Testing
In order to test Java Rules using JUnit and mockito the OpenHAB Eventbus has 
a mock implementation https://github.com/seaside1/jrule/tree/main/src/main/java/org/openhab/automation/jrule/internal/test
This implementation is really simple and only supports ItemStateChangedEvents for now. Feel free to expand it
and anything needed in order to test Java Rules. If possible submit a PR with changes

Using the MockEventBus you can add a textfile with actual OpenHAB Events. You typically find these events looking at
/var/log/openhab/events.log

```
2022-03-15 15:30:26.575 [INFO ] [openhab.event.ItemStateChangedEvent ] - Item 'MyLastSeen1' changed from 2022-03-15T15:28:36.902+0100 to 202`2-03-15T15:29:36.891+0100
2022-03-15 15:30:31.375 [INFO ] [openhab.event.ConfigStatusInfoEvent ] - ConfigStatusInfo [configStatusMessages=[]]
2022-03-15 15:30:34.602 [INFO ] [openhab.event.ItemStateChangedEvent ] - Item 'MyLastSeen2' changed from 2022-03-15T15:30:17.975+0100 to 2022-03-15T15:30:34.580+0100
2022-03-15 15:30:34.613 [INFO ] [openhab.event.ItemStateChangedEvent ] - Item 'MyLastSeen1' changed from 2022-03-15T15:30:17.986+0100 to 2022-03-15T15:30:34.595+0100
2022-03-15 15:30:37.446 [INFO ] [hab.event.GroupItemStateChangedEvent] - Item 'gPower' changed from 916.7 to 823.5 through MyPower1
2022-03-15 15:30:37.448 [INFO ] [openhab.event.ItemStateChangedEvent ] - Item 'MyPower1' changed from 183.3 to 90.1
2022-03-15 15:30:38.445 [INFO ] [openhab.event.ItemStateChangedEvent ] - Item 'MyPower1' changed from 90.1 to 158.1
2022-03-15 15:30:38.447 [INFO ] [hab.event.GroupItemStateChangedEvent] - Item 'gPower' changed from 823.5 to 891.5 through MyPower2
2022-03-15 15:30:39.525 [INFO ] [openhab.event.ItemStateChangedEvent ] - Item 'MyPower2' changed from 158.1 to 304.9
2022-03-15 15:30:39.527 [INFO ] [hab.event.GroupItemStateChangedEvent] - Item 'gPower' changed from 891.5 to 1038.3 through MyPower2
2022-03-15 15:30:40.645 [INFO ] [openhab.event.ItemStateChangedEvent ] - Item 'MyPower2' changed from 304.9 to 168.5
2022-03-15 15:30:40.646 [INFO ] [hab.event.GroupItemStateChangedEvent] - Item 'gPower' changed from 1038.3 to 901.9 through MyPower2
```

We `add a rule: https://github.com/seaside1/jrule-user/blob/main/src/main/java/org/openhab/automation/jrule/rules/user/MyFirstRules.java

```java
    @JRuleName("testRuleZwave")
   ` @JRuleWhen(item = _MyPower1.ITEM, trigger = _MyPower1.TRIGGER_CHANGED)
    public synchronized void myFirstRule(JRuleEvent event) {
        logInfo("MyPower1 Changed! from: {} to: {}", event.getOldState(), event.getState());
    }
```
 
 In` Order to test the rule, and see that myFirstRule is trigged two time we can add the following test:
 
```java
  @Test
   ` public void testMyRule() {
        JRuleEngine engine = JRuleEngine.get();
        JRuleConfig config = new JRuleConfig(new HashedMap<>());
        engine.setConfig(config);
        engine.add(myFirstRules);
        JRuleMockedEventBus eventBus = new JRuleMockedEventBus("eventlog.txt");
        eventBus.start();
        verify(myFirstRules, times(2)).myFirstRule(Mockito.any(JRuleEvent.class));
    }
```
 
 Th`is will simulate the eventbus, and add all events in the eventlog.txt. The engine will trigger the rule and the mock will verify
 that it is called twice.
 
