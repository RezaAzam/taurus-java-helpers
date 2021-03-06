# 1.9 (11th of January, 2019)
* `Added` : better error messages for loaded classes

# 1.8 (2nd of November, 2018)
* `Added` : escape CSV function for error messages

# 1.7 (1st of November, 2018)
* `Fixed` : multiline error messages for CSV report
* `Changes` : `start_time` to include milliseconds as decimal

# 1.6 (21th of September, 2018)
* `Fixed` : detect `ERROR` or `FAILED` sampler from Exception type

# 1.5 (19th of September, 2018)
* `Changes` : use `FINE` level logging in ClassFilter
* `Fixed` : JUnit 4 runner set `STATUS_BROKEN` instead of `STATUS_FAILED`

# 1.4 (1st of August, 2018)
* `Changes`: remove `target_` property. All target *.jar files should be included in CLASSPATH
* `Changes`: TestNG listener write samplers in background thread
* `Fixed`: detect JUnit 5 test methods

# 1.3 (23th of July, 2018)
* `Added`: parallelize tests

# 1.2 (12th of July, 2018)
* `Added`: support JUnit 5
* `Fixed`: missed `Ignore` tests

## 1.1 (6th of July, 2018)
* `Added`: choice test items and categories for run
* `Added`: pass on properties to Java System properties

## 1.0 (4th of July, 2018)
