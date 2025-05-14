
## package

package gpg:sign -Dmaven.test.skip=true -Dspring.version=5.3.22 -Dspring-boot-current.version=2.7.3 -Dslf4j.version=1.7.36 -pl mee-timed

## sign
+ use win cmd
certUtil -hashfile mee-timed-1.0.2.jar md5 >> mee-timed-1.0.2.jar.md5
certUtil -hashfile mee-timed-1.0.2.jar sha1 >> mee-timed-1.0.2.jar.sha1

certUtil -hashfile mee-timed-1.0.2-sources.jar md5 >> mee-timed-1.0.2-sources.jar.md5
certUtil -hashfile mee-timed-1.0.2-sources.jar sha1 >> mee-timed-1.0.2-sources.jar.sha1

certUtil -hashfile mee-timed-1.0.2-javadoc.jar md5 >> mee-timed-1.0.2-javadoc.jar.md5
certUtil -hashfile mee-timed-1.0.2-javadoc.jar sha1 >> mee-timed-1.0.2-javadoc.jar.sha1

certUtil -hashfile mee-timed-1.0.2.pom md5 >> mee-timed-1.0.2.pom.md5
certUtil -hashfile mee-timed-1.0.2.pom sha1 >> mee-timed-1.0.2.pom.sha1

## clear content

+ use git bash 
sed -i '1d;3d' mee-timed-1.0.2.jar.md5
sed -i '1d;3d' mee-timed-1.0.2.pom.md5
sed -i '1d;3d' mee-timed-1.0.2-javadoc.jar.md5
sed -i '1d;3d' mee-timed-1.0.2-sources.jar.md5

sed -i '1d;3d' mee-timed-1.0.2.jar.sha1
sed -i '1d;3d' mee-timed-1.0.2.pom.sha1
sed -i '1d;3d' mee-timed-1.0.2-javadoc.jar.sha1
sed -i '1d;3d' mee-timed-1.0.2-sources.jar.sha1

