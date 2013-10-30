This is a library in which we put utility classes that we feel java is missing

IMPORTANT: Do not push a library class or classes to the master branch until:
	   1. It has no known bugs
	   2. It has no pending bug fixes
	   3. It has no important improvements
	   4. It MUST have documentation
	   5. Make sure to use eclipse's Clean Up... command and use the included
		   Clean Up settings (you can modify them if there is something you
		   really don't like)

Currently in the library:
	-com.lab_programming.util.RegularTokenizer
		Why I am bothering:
			The StringTokenizer has been declared obsolete and is supposedly
				superseded by the Matcher/Pattern classes and String's split()
				however it does not replace all the functionality such as the
				ease of looping through a string and getting the delimiters too
		Description:
			Like the StringTokenizer class in java.util but can handle
				regular expressions for defining the delimeters
		Status:
			No known bugs and functional documentation is all that is left
		Bugs: None known
		To do (prioritized in each category from most to least important):
			Required Bug Fixes: None
			Important Improvements:
				-Documentation
			Future Plans:
				-Allow it to be constructed from a pattern (or maybe
					even a matcher)
				-Make it extend StringTokenizer
				-Make it thread-safe
				-Make it iterable
				-Make it return "" in between delimiters that are back to back
		Comments:
			
	
