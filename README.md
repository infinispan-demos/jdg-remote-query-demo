# DataGridDemo



https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?product=data.grid&downloadType=distributions


Demo1 - distribute cache

/home/sgutierr/Escritorio/DemoDataGrid/Demo1

Demo2 - Tasks - remote server

../jboss-datagrid-6.5.0-server/bin/lustered.sh -Djboss.socket.binding.port-offset=100 -Djboss.node.name=nodo1
../jboss-datagrid-6.5.0-server/bin/clustered.sh -Djboss.socket.binding.port-offset=500 -Djboss.node.name=nodo2
../jboss-datagrid-6.5.0-server/bin/clustered.sh -Djboss.socket.binding.port-offset=900 -Djboss.node.name=nodo3
../jboss-eap-6.4_s2/bin/standalone.sh
../jboss-datagrid-6.5.0-server/bin/jconsole.sh

* Near cache
https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Data_Grid/6.5/html/Developer_Guide/Configuring_Near_Caches.html

mvn clean package jboss-as:deploy


Demo3 - Spring

mvn spring-boot:run
http://localhost:8080/infinispan-spring

