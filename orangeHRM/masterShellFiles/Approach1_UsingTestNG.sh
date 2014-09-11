
#Approach 1 - To execute test suites using TestNg commands from terminal (Note: without build.xml)
#Disadvantage - TestNg report for each test suite will be replaced under test-output directory
#Note: To overcome this disadvantage, utilize Approach2 which utilizes build.xml 



cd /c/Users/Yash/git/SeleniumLearnExploreContribute/orangeHRM/suites/webSuites

java -cp /c/Users/Yash/Downloads/Automation/jars/new/allJars/*:/c/Users/Yash/git/SeleniumLearnExploreContribute/orangeHRM/bin org.testng.TestNG Suite1_RegressionLoginLogout.xml
java -cp /c/Users/Yash/Downloads/Automation/jars/new/allJars/*:/c/Users/Yash/git/SeleniumLearnExploreContribute/orangeHRM/bin org.testng.TestNG ReportParseAndGoogleDocUpdate.xml

java -cp /c/Users/Yash/Downloads/Automation/jars/new/allJars/*:/c/Users/Yash/git/SeleniumLearnExploreContribute/orangeHRM/bin org.testng.TestNG Suite2_AnnotationInvalidLoginTests.xml
java -cp /c/Users/Yash/Downloads/Automation/jars/new/allJars/*:/c/Users/Yash/git/SeleniumLearnExploreContribute/orangeHRM/bin org.testng.TestNG ReportParseAndGoogleDocUpdate.xml
