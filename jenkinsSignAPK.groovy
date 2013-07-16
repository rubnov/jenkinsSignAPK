import hudson.model.*

// This script signs the .apk file in your Jenkins workspace  
// Prerequisite: keytool -genkey -v -keystore my-release-key.keystore -alias alias_name -keyalg RSA -validity 10000

def keystoreName    = "my-release-key.keystore"
def keystoreAlias   = "alias_name"

def getApkFile(fileOrDir){
        def filePattern = ~/.*\.(apk)$/
        def found = ""
        if(fileOrDir.isDirectory()){
            fileOrDir.eachFileRecurse{
                if(!it.isDirectory() && it.getName() =~ filePattern)
                    found = it.getAbsolutePath()
            }
        }else{
            if(fileOrDir.getName() =~ filePattern){
                found = fileOrDir.getAbsolutePath()
            }
        }
        return found
}

def buildFile = getApkFile(new File(build.getWorkspace().getRemote()))

println("Signing " + buildFile)
def signProc = """jarsigner -verbose -keystore ${keystoreName} ${buildFile} ${keystoreAlias}"""
println(signProc)
println(signProc.execute().text)

return 0