#!/bin/bash

mvn clean install

$JBOSS_HOME/bin/add-user.sh -a -u quickuser -p quick-123
$JBOSS_HOME/bin/add-user.sh -a -u quickuser1 -p quick123+
$JBOSS_HOME/bin/add-user.sh -a -u quickuser2 -p quick+123

$JBOSS_HOME/bin/jboss-cli.sh -c --file=install-domain.cli
$JBOSS_HOME/bin/jboss-cli.sh -c --file=deploy-domain.cli

$JBOSS_HOME/bin/jboss-cli.sh -c "reload --host=master"

echo "To run the client"
echo "cd client ; mvn exec:java"
