## Hypothetical Travel System

### Key Concepts:
**Touch On:** Onboarding a bus, passengers taps their credit card (identified by a Hashed Number called as Primary Account Number) which is called as Touch On.

**Touch Off:** When passenger gets off the bus, they tap their card again which is called a Touch Off.

**Amount to Charge:** The amount to charge the passenger will be determined where they Touch On and where the Touch Off. The amount is determined as follows:
- Trip Between Stop A and Stop B costs $4.50
- Trip Between Stop B and Stop C costs $6.25
- Trip Between Stop A and Stop C costs $8.45

**Travel Direction:** The above Amount to Charge applies to travel in either direction. This means that the same amount is charged if a passenger Touch On at Stop A and Touch Off at Stop B OR they can Touch On at Stop B and Touch Off at Stop A.

### Types of Trips
- **Completed Trips:** If the passenger Touch On at one stop and Touch Off at another stop, this is treated as a completed trip. The amount to charge the passenger is determined by the above Amount to Charge section. E.g: Touch On at Stop A and Touch Off at Stop C is a completed trip and passenger is charged $8.45.
- **Incomplete Trips:** If the passenger Touch On at one stop and forget to Touch Off at another stop, this is treated as an incomplete trip. The passenger in this case is charged the maximum possible fare, where they could have travelled to. Eg: A passenger Touch On at Stop B and does not Touch Off, they could travelled to either Stop A ($4.5) or Stop C ($6.25). In this case, they will be charged the higher value ($6.25).
- **Cancelled Trip:** If the passenger Touch On and Touch Off at the same stop, this is called a cancelled trip and the passenger would not be charged.

### Assumptions
The system take into consideration following assumptions -
- The system assumed the touch data comes in a sequential order that is an entry an **ON** entry `i` place will have an OFF entry at `i+n` place
- A touch is valid is there is a **ON** and **OFF** entry for corresponding **PAN**, **CompanyID** and **BusId**
- The touch **OFF** should be later than touch **ON**.
- Any touch **OFF** with a matching touch **ON** is considered failed and is marked as `Touch Type ON is missing`

# Configurations
The application uses `application.yaml` for its configurations, example

```
fair:
  currency: $
  rules:
    -
      source: StopA
      destination: StopB
      cost: 4.50
    -
      source: StopB
      destination: StopC
      cost: 6.25
    -
      source: StopA
      destination: StopC
      cost: 8.45

input-file:
  type: csv
  delimiter: ","
  dateTimeFormat: "dd-MM-yyyy HH:mm:ss"
  headers:
    - ID
    - DateTimeUTC
    - TouchType
    - StopID
    - CompanyID
    - BusID
    - PAN
```

New fair rules can be added to the application under the `fair.rule` section, example adding this to `fair.rules` section will add one more fair rule for processing -
```
-
  source: StopA
  destination: StopD
  cost: 10.50
```


### Build
This application use **gradle** as its build plugin, please refer to HELP.md for any addition gradle references.

### Running the application

The application will read from a csv file in this example we will use `sample-input.csv`
This filename can be passed as command-line argument to the application for example -
> --filename=sample-input.csv

So the application can be run as follows -
> ./gradlew booRun --args=--filename=sample-input.csv

Where `sample-input.csv` is the input filename.
This will generate following output files in running directory -
- trips.csv
- unprocesssableTouchData.csv
- summary.csv



### Testing the application

This application test suite can be run using the gradle task as follows

> ./gradlew cleanTest test
