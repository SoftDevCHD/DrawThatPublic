# pictionary_game

## Versions

#### Version 0.1 
Feel free to modify anything already built to better suit what you think would work, but please document what you worked on so we can understand the progress.  
- Created a basic database for us to use in **Back4app**.
- Created a **LoginActivity** that works for existing users, but does not allow creation of new account.  You can use user: `admin` pw: `admin`
- Created a **MainActivity** that is composed of a `fragment` and a `bottomNavigationView` for navigating the various fragments.  
*- Charles Kypros*

---
## TODO
- The following **TODO** lists are not complete, please feel free to add features as you think of things, or check things off as you complete them.  
- If you complete somethings could you append your name / date to the end of it like this: 
  - [x] Did a example task *(Charles Kypros, 3/23/21)*
- I have not mentioned functionality like sharing pictures of work created yet, because I think its important to get the minimum viable product running.


### TODO (Programming)
- [ ] Create functionality for user to make new account
- [ ] Create interface and class for teams within group
  - [ ] User needs to be able to set how many teams, and name them
- [ ] Finish the **GameFragment** 
  - [ ] It needs to get a random "Phrase" card from the database
  - [ ] Make a `ProgressBar` for a timer, maybe set to *60 seconds*?
    - [ ] Create sound alert when out of time.
  - [ ] Create a way for user to indicate they are done
    - [ ] Stop the timer if needed
    - [ ] Allow them to specify team winner or nobody
- [ ] Finish the **StatusFragment**
  - [ ] Need to have it show a score for each team
  - [ ] Maybe some other fun statistics?
- [ ] Finish the **ProfileFragment** 
  - [ ] Let the group take a picture of themselves to be represented on their profile (profile photo)
  - [ ] Perhaps have it show a `RecyclerView` of submitted drawings?
  
 ### TODO (Design)
 - [ ] We need to make a name, for obvious reasons we can't just call it *Pictionary*
 - [ ] We need a logo
 - [ ] Various Graphics for use throughout
 - [ ] Icons for our bottom navigaton bar
 - [ ] Design color palette for app

---
## The following is a little diagram I made to indicate how we the app will flow
![diagram](https://user-images.githubusercontent.com/35363316/112211092-b91fd500-8bd8-11eb-81c5-c1d73ffc38ee.png)

