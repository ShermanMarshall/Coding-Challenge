cp /etc/passwd cc_passwd
cp /etc/group cc_group

export ETC_PASSWD=`pwd`/cc_passwd
export ETC_GROUP=`pwd`/cc_group

echo "user and group info will be read from the following locations"
echo $ETC_PASSWD
echo $ETC_GROUP

mvn clean; mvn install -DskipTests; java -jar target/Coding-Challenge-0.1.jar

