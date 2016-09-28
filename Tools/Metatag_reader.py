import sys
import operator

#Pass in Numbered_tags.txt and Tallied_tags.txt in
def main(fileName1, fileName2):
	metaTagDict = {}
	metaTagDict = tallyCounter(fileName2, metaTagDict)
	metaTagDict = tagCounter(fileName1, metaTagDict)

	outFileName = "All_tallies"
	writeToFile(metaTagDict, outFileName)

#Go through the input file and total up the number per metatag
def tagCounter(fileName, metaTagDict):
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
		#When an integer is encountered, that's the count
		for token in line.split():
			if is_int(token):
				count = int(token)
			else:
				metatag += token + " "

		#For existing metatags, add onto the current amount
		if metatag in metaTagDict.keys():
			metaTagDict[metatag] = metaTagDict[metatag] + int(count)

		#For new metatags, record the count
		metaTagDict[metatag] = int(count)

	return metaTagDict

def tallyCounter(fileName, metaTagDict):
	metaTagFile = open(fileName + ".txt", 'r')
	metaTagDict = {}

	#Read through lines of the file, parsing it into a dictionary
	for line in metaTagFile:
		metatag = ""
		count = 0

		#Ignore the line if it contains "City"
		if "City" in line:
			continue

		#Format is metatag followed by count
		#Read in the strings as part of the tag name
		#When an integer is encountered, that's the start of the tally
		for token in line.split():
			if is_int(token):
				count = len(token)
			else:
				metatag += token + " "

		#For existing metatags, add onto the current amount
		if metatag in metaTagDict.keys():
			metaTagDict[metatag] = metaTagDict[metatag] + int(count)

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

def is_int(string):
	try:
		int(string)
		return True
	except ValueError:
		return False

if __name__ == 	"__main__":
	main(sys.argv[1], sys.argv[2])