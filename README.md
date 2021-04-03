Original App Design Project - README Template
===

# DrawThat

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
App similar to pictionary game. DrawThat is played locally with a group of people. Allows for sharing of pictures created while playing.

### App Evaluation
- **Category:** Game / Entertainment
- **Mobile:** This app would work best on a mobile device. It could potentially be used on computers and laptops later, but mobile would likely deliver the best experience.
- **Story:** App facilitates playing a pictionary-like game in-person. Keeps track of score, provides words to draw, and allows for user sharing.
- **Market:** App is good for any age group. People would register as a group rather than as an individual.
- **Habit:** App could be used whenever a group of people would like to play.
- **Scope:** Would start off with necessary features such as keeping score, providing words, and sharing with others. Could then expand on this in various ways such as allowing the user to draw pictures on the app and leaving comments on posts.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can register as a group.
* User can login.
* User can pick number of teams.
* App picks random word for user to draw.
* App keeps time for drawing phase.
* Game loop (start round, app picks word, user draws, end round).
* User can use app to keep score.
* User can take a picture of their drawings.
* User can post pictures.
* User can see other groups' pictures in feed.
* ...

**Optional Nice-to-have Stories**

* User can name the teams.
* User can select difficulty of words.
* ...

### 2. Screen Archetypes

* Login
   * User can register with a group
   * User can sign in or sign up
   * User chooses the number of teams participating
* Stream
   * Display random words to be drawn by players
   * Clock/Timer for increased competitiveness. Basically, users have a time limit to draw
   * Display teams and their total scores
* Detail
   * See drawings of user's team or other teams
   * See team members
   * See description of word to draw
* Creation
   * User can take picture of drawing
   * Post pictures with comments to social media or feed

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Detail
* Creation

**Flow Navigation** (Screen to Screen)

* Login
   * Stream
* Stream
   * Detail
* Detail
   * Creation
   * Stream
* Creation
   * Detail
   * Stream

## Wireframes
<img src="wireframe.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
