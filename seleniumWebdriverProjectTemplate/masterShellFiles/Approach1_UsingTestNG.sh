
#Approach 1 - To execute test suites using TestNg commands from terminal (Note: without build.xml)
#Disadvantage - TestNg report for each test suite will be replaced under test-output directory
#Note: To overcome this disadvantage, utilize Approach2 which utilizes build.xml 



cd /c/Users/Yash/git/SeleniumLearnExploreContribute/seleniumWebdriverProjectTemplate/suites/webSuites

java -cp /c/Users/Yash/Downloads/Automation/jars/new/allJars/*:/Users/Yash/git/SeleniumLearnExploreContribute/seleniumWebdriverProjectTemplate/bin org.testng.TestNG Template1_RunAllTestNgTests.xml
java -cp /c/Users/Yash/Downloads/Automation/jars/new/allJars/*:/Users/Yash/git/SeleniumLearnExploreContribute/seleniumWebdriverProjectTemplate/bin org.testng.TestNG ReportParseAndGoogleDocUpdate.xml

java -cp /c/Users/Yash/Downloads/Automation/jars/new/allJars/*:/Users/Yash/git/SeleniumLearnExploreContribute/seleniumWebdriverProjectTemplate/bin org.testng.TestNG Template2_RunSpecificTestngGroupsTest.xml
java -cp /c/Users/Yash/Downloads/Automation/jars/new/allJars/*:/Users/Yash/git/SeleniumLearnExploreContribute/seleniumWebdriverProjectTemplate/bin org.testng.TestNG ReportParseAndGoogleDocUpdate.xml
