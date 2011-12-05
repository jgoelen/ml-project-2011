package be.kuleuven.dtai.ml


int nlinks = 0
int ntweets = 0

new File("/Users/jgoelen/rsync-shares/kul-courses/adb-2011/hw6/data/twitter").eachFileRecurse { tweet ->
	
	if(tweet.getName().endsWith("txt") ){
	
	if(tweet.getText().contains("http")){
		
		nlinks++

	}
	
	ntweets++
	}
}


println "ntweets  ${ntweets}, nlinks = ${nlinks}"