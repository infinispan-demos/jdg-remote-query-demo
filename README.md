# DataGridDemo

This is a demo showing the remote query capability in JBoss Data Grid. To tun this demo, you will need to download
[JBoss Data Grid](https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?product=data.grid&downloadType=distributions) and [JBoss EAP](https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?product=appplatform&downloadType=distributions). This demo has been tested with JBoss Data Grid 7.1 and JBoss EAP 7.0.

## Screenshot

![Alt text](/docs/screenshot.png?raw=true "Demo Screenshot")

## Instructions

* Create a directory called ```demo``` somewhere convenient on your file system.

* Unzip EAP into this directory so you have the folder ```demo/jboss-eap-7.0```.

* Create a management user by running ```jboss-eap-7.0/bin/add-user.sh```, username/password should be admin/admin

* Similarly unzip Data Grid into the ```demo``` directory so you have the folder ```demo/jboss-datagrid-7.1.0-server```.

* Create a management user by running ```jboss-datagrid-7.1.0-server/bin/add-user.sh```, username/password should be admin/admin

* Clone this repository in the demo directory so you have the folder ```demo/jdg7_demo```

* Configure the distributed ```tasks``` cache in ```jboss-datagrid-7.1.0-server/standalone/configuration/clustered.xml```. Look for the stanza:

```
<subsystem xmlns="urn:infinispan:server:core:8.4" default-cache-container="clustered">
    <cache-container name="clustered" default-cache="default" statistics="true">
        <transport lock-timeout="60000"/>
        <global-state/>
        <distributed-cache-configuration name="transactional">
            <transaction mode="NON_XA" locking="PESSIMISTIC"/>
        </distributed-cache-configuration>
        ...
```

At the end of the list of caches, add a new cache as follows:

```
<distributed-cache name="tasks" statistics="true" owners="2" remote-timeout="30000" start="EAGER">
    <locking isolation="NONE" acquire-timeout="30000" concurrency-level="1000" striping="false"/>
    <transaction mode="NONE"/>
</distributed-cache>
```

* Start JDG in domain mode which will start two servers automatically:

```
bin/domain.sh

```

* Go into the management console at ```http://localhost:9990``` and make sure that the tasks cache is shown

* Start EAP as per below, note we start EAP with a port offset of a 1000 so ports don't conflict with JDG:

```
jboss-eap-7.0/bin/standalone.sh -b 127.0.0.1 -bmanagement=127.0.0.1 -Djboss.socket.binding.port-offset=1000
```

* Change into the ```jdg7_demo``` and build and deploy the application with ```mvn clean package wildfly:deploy```

* Run the demo in a browser at ```http://localhost:9080/jdgdemo```