# DataGridDemo

This is a demo showing the remote query capability in JBoss Data Grid. To tun this demo, you will need to download
[JBoss Data Grid](https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?product=data.grid&downloadType=distributions) and [JBoss EAP](https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?product=appplatform&downloadType=distributions). This demo has been tested with JBoss Data Grid 7.1 and JBoss EAP 7.0.

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

* Create three instances by copying the standalone directory as follows:

```
cp -r jboss-datagrid-7.1.0-server/standalone jboss-datagrid-7.1.0-server/standalone1
cp -r jboss-datagrid-7.1.0-server/standalone jboss-datagrid-7.1.0-server/standalone2
cp -r jboss-datagrid-7.1.0-server/standalone jboss-datagrid-7.1.0-server/standalone3
```

* Create the bash script ```startInstance.sh``` in ```demo```:

```
if [ $1 -eq 0 ]
  then
    echo "Please provide an instance number (1..3)"
    exit 1
fi
jboss-datagrid-7.1.0-server/bin/standalone.sh -c clustered.xml -b 127.0.0.1 -bmanagement=127.0.0.1 -Djboss.server.base.dir=jboss-datagrid-7.1.0-server/standalone${1} -Djboss.node.name=jdg${1} -Djboss.socket.binding.port-offset=${1}
```

* Start the three cache instances as follows:


```
./startInstance.sh 1
./startInstance.sh 2
./startInstance.sh 3
```

* Go into the management console at ```http://localhost:9991``` and make sure that the tasks cache is shown

* Start EAP as follows:

```
jboss-eap-7.0/bin/standalone.sh -b 127.0.0.1 -bmanagement=127.0.0.1
```

* Change into the ```jdg7_demo``` and build the application with ```mvn clean package```

* Deploy the application using ```mvn wildfly:deploy```

* Run the demo in a browser at ```http://localhost:8080/jdgdemo```