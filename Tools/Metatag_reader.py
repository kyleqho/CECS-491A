import sys
import operator

#Pass in Numbered_tags.txt and Tallied_tags.txt
def main(fileName1, fileName2):
	metaTagDict = {}
	metaTagDict = tagCounter(fileName2, metaTagDict, tally=True)
	metaTagDict = tagCounter(fileName1, metaTagDict)

	outFileName = "All_tallies"
	writeToFile(metaTagDict, outFileName)

#Go through the input file and total up the number per metatag
#If tally flag is set, count the tallies, rather than add a number
def tagCounter(fileName, metaTagDict, tally=False):
	#Open input file for read
	metaTagFile = open(fileName + ".txt", 'r')
	#metaTagDict = {}

	#Read through lines of the file, parsing it into a dictionary
	for line in metaTagFile:
		metatag = ""
		count = 0

		#Ignore the line if it contains "City"
		if line[:4] == "City":
			continue

		#Format is metatag followed by count
		#Read in the strings as part of the tag name
		#It will either encounter an integer to add on to the count
		#Or encounter the tallies (sequence of 1's), count the tallies, and
		#add the tallies on
		for token in line.split():
			if is_int(token):
				if not tally:
					count = int(token)
				else:
					count = len(token)
			else:
				metatag += token + " "

		#For existing metatags, add onto the current amount
		if metatag in metaTagDict.keys():
			metaTagDict[metatag] += int(count)
		else:
			#For new metatags, record the count
			metaTagDict[metatag] = int(count)

	return metaTagDict

#Write to the output file
def writeToFile(metaTagDict, fileName):
	#Create output file for writing results
	outFileName = fileName + ".txt"
	resultsFile = open(outFileName, 'w')

	#Sort the dictionary by count, largest to smallest
	for key in sorted(metaTagDict, key=metaTagDict.get, reverse=True):
		resultsFile.write(str(metaTagDict[key]) + "\t" + key + "\n")

#Checks to see if the given string is an integer
def is_int(string):
	try:
		int(string)
		return True
	except ValueError:
		return False

#Call from command line requires 2 file names as input
if __name__ == 	"__main__":
	main(sys.argv[1], sys.argv[2])