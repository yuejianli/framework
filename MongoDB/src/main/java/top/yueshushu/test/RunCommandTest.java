package top.yueshushu.test;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 运行命令
 *
 * @author yuejianli
 * @date 2022-07-18
 */
@SpringBootTest
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
public class RunCommandTest {
	@Resource
	private MongoTemplate mongoTemplate;
	
	/**
	 执行 mongoDB 自定义命令，详情可以查看：https://docs.mongodb.com/manual/reference/command/
	 */
	@Test
	public void runCommandTest(){
		// 自定义命令
		String jsonCommand = "{\"buildInfo\":1}";
		// 将 JSON 字符串解析成 MongoDB 命令
		Bson bson = Document.parse(jsonCommand);
		// 执行自定义命令
		Document document = mongoTemplate.getDb().runCommand(bson);
		
		/**
		 document :Document{{version=5.0.6, gitVersion=212a8dbb47f07427dae194a9c75baec1d81d9259, modules=[], allocator=tcmalloc,
		 javascriptEngine=mozjs, sysInfo=deprecated, versionArray=[5, 0, 6, 0], openssl=Document{{running=OpenSSL 1.0.1e-fips 11 Feb 2013,
		 compiled=OpenSSL 1.0.1e-fips 11 Feb 2013}}, buildEnvironment=Document{{distmod=rhel70, distarch=x86_64,
		 cc=/opt/mongodbtoolchain/v3/bin/gcc: gcc (GCC) 8.5.0, ccflags=-Werror -include mongo/platform/basic.h -fasynchronous-unwind-tables -ggdb
		 -Wall -Wsign-compare -Wno-unknown-pragmas -Winvalid-pch -fno-omit-frame-pointer -fno-strict-aliasing -O2 -march=sandybridge
		 -mtune=generic -mprefer-vector-width=128 -Wno-unused-local-typedefs -Wno-unused-function -Wno-deprecated-declarations
		 -Wno-unused-const-variable -Wno-unused-but-set-variable -Wno-missing-braces -fstack-protector-strong -Wa,--nocompress-debug-sections
		 -fno-builtin-memcmp, cxx=/opt/mongodbtoolchain/v3/bin/g++: g++ (GCC) 8.5.0, cxxflags=-Woverloaded-virtual -Wno-maybe-uninitialized
		 -fsized-deallocation -std=c++17, linkflags=-Wl,--fatal-warnings -pthread -Wl,-z,now -fuse-ld=gold -fstack-protector-strong -Wl,
		 --no-threads -Wl,--build-id -Wl,--hash-style=gnu -Wl,-z,noexecstack -Wl,--warn-execstack -Wl,-z,relro -Wl,--compress-debug-sections=none -Wl,
		 -z,origin -Wl,--enable-new-dtags, target_arch=x86_64, target_os=linux, cppdefines=SAFEINT_USE_INTRINSICS 0 PCRE_STATIC NDEBUG _XOPEN_SOURCE
		 700 _GNU_SOURCE _FORTIFY_SOURCE 2 BOOST_THREAD_VERSION 5 BOOST_THREAD_USES_DATETIME BOOST_SYSTEM_NO_DEPRECATED
		 BOOST_MATH_NO_LONG_DOUBLE_MATH_FUNCTIONS BOOST_ENABLE_ASSERT_DEBUG_HANDLER BOOST_LOG_NO_SHORTHAND_NAMES
		 BOOST_LOG_USE_NATIVE_SYSLOG BOOST_LOG_WITHOUT_THREAD_ATTR ABSL_FORCE_ALIGNED_ACCESS}},
		 bits=64, debug=false, maxBsonObjectSize=16777216, storageEngines=[devnull, ephemeralForTest, wiredTiger], ok=1.0}}
		 */
		log.info("document :{}",document);
	}
}
