
## package

package gpg:sign -Dmaven.test.skip=true -pl mee-timed

## sign
+ use win cmd
certUtil -hashfile mee-timed-1.0.1.jar md5 >> mee-timed-1.0.1.jar.md5
certUtil -hashfile mee-timed-1.0.1.jar sha1 >> mee-timed-1.0.1.jar.sha1

certUtil -hashfile mee-timed-1.0.1-sources.jar md5 >> mee-timed-1.0.1-sources.jar.md5
certUtil -hashfile mee-timed-1.0.1-sources.jar sha1 >> mee-timed-1.0.1-sources.jar.sha1

certUtil -hashfile mee-timed-1.0.1-javadoc.jar md5 >> mee-timed-1.0.1-javadoc.jar.md5
certUtil -hashfile mee-timed-1.0.1-javadoc.jar sha1 >> mee-timed-1.0.1-javadoc.jar.sha1

certUtil -hashfile mee-timed-1.0.1.pom md5 >> mee-timed-1.0.1.pom.md5
certUtil -hashfile mee-timed-1.0.1.pom sha1 >> mee-timed-1.0.1.pom.sha1

## clear content

+ use git bash 
sed -i '1d;3d' mee-timed-1.0.1.jar.md5
sed -i '1d;3d' mee-timed-1.0.1.pom.md5
sed -i '1d;3d' mee-timed-1.0.1-javadoc.jar.md5
sed -i '1d;3d' mee-timed-1.0.1-sources.jar.md5

sed -i '1d;3d' mee-timed-1.0.1.jar.sha1
sed -i '1d;3d' mee-timed-1.0.1.pom.sha1
sed -i '1d;3d' mee-timed-1.0.1-javadoc.jar.sha1
sed -i '1d;3d' mee-timed-1.0.1-sources.jar.sha1

