Custom statement processor for Acme Inc.

Instructions for use to process new customer statements:

1) Add new customer statement files to the folder 'delivered_files'.
The only allowed name/format of the files are:
  - records.csv (ISO-8859 text)
  - records.xml (XML 1.0 document text, UTF-8 Unicode text, with very long lines, with no line terminators)
  
2) Run the processor by running the main class in Application.kt. A new validation report will be created in the folder 'reports'.

Future improvements include:
  - improve processing performance for very large files
  - provide User Ineterface to deliver new customer statements and to display the reports
