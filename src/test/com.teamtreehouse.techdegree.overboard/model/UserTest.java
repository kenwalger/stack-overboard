package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Ken on 05/01/2017.
 */
public class UserTest {

  private Board mBoard;
  private User mQuestionUser;
  private User mAnswerUser;
  private User mBoardUser;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    mBoard = new Board("Java");
    mQuestionUser = mBoard.createUser("Questioner");
    mAnswerUser = mBoard.createUser("Answerer");
    mBoardUser = mBoard.createUser("BoardUser");
  }

  @Test
  public void upVotingQuestionIncrementsQuestionersReputationByFivePoints() throws Exception {
    //Arrange : Question is asked by mQuestionUser
    Question question = mQuestionUser.askQuestion("Java Interfaces can extend");

    //Action : mBoardUser can UpVote the Question
    mBoardUser.upVote(question);

    //Assert : Verify Questioners reputation Goes up by five points
    assertEquals(5,mQuestionUser.getReputation());
  }

  @Test
  public void downVotingQuestionDoesNotAffectReputation() throws Exception {
    //Arrange : mQuestion asks Question
    Question question = mQuestionUser.askQuestion("Java Interfaces can extend");

    //Action : mtBoardUser DownVote the Question
    mBoardUser.downVote(question);

    //Assert: Verify Questioners reputation is not affected
    assertEquals(0,mQuestionUser.getReputation());
  }

  @Test
  public void upVotingAnswerIncrementsAnswererReputationByTenPoints() throws Exception {
    //Arrange : Question is asked by mQuestionUser and Answered by mAnswerUser
    Question question = mQuestionUser.askQuestion("Java Interfaces can extend");
    Answer answer = mAnswerUser.answerQuestion(question,"Multiple Interfaces");

    //Action : mQuestionUser can Upvote answer by mBoardUser
    mQuestionUser.upVote(answer);

    //Assert : Verify Answers reputation Increments by 10
    assertEquals(10,mAnswerUser.getReputation());
  }

  @Test
  public void downVotingAnswerDecrementsAnswererReputationByOnePoint() throws Exception {
    //Arrange : mQuestionUser asks Question,mAnswerUser answers Answer
    Question question = mQuestionUser.askQuestion("Java Interfaces can extend");
    Answer answer = mAnswerUser.answerQuestion(question,"Multiple Interfaces");

    //Action : mBoarduser downVote Answer
    mBoardUser.downVote(answer);

    //Assert: Verify Answers reputation Decrements by 1
    assertEquals(-1,mAnswerUser.getReputation());
  }

  @Test
  public void questionerAcceptAnswerIncrementsAnswererReputationByFifteenPoints() throws Exception {
    //Arrange : Question is asked by mQuestionUser and Answered by mAnswer User
    Question question = mQuestionUser.askQuestion("Java Interfaces can extend");
    Answer answer = mAnswerUser.answerQuestion(question,"Multiple Interfaces");

    //Action : mQuestionUser accepts the answer given by mAnswerUser
    mQuestionUser.acceptAnswer(answer);

    //Assert : Verify Answerer's a 15 point reputation Boost
    assertEquals(15,mAnswerUser.getReputation());
  }

  @Test
  public void questionerUpVotingQuestionIsNotAllowed() throws Exception {
    //@ Rule changes the Behaviour of Testing
    // Voting Exception is Catches Inside Class when questioner UpVotes his Question
    thrown.expect(VotingException.class);
    thrown.expectMessage("You cannot vote for yourself!");

    //Arrange : mQuestionUser is asking Question
    Question question = mQuestionUser.askQuestion("Is it possible to access non-static members" +
        "without instance of the class");

    //Action : mQuestionUser is UpVoting Question
    mQuestionUser.upVote(question);
  }

  @Test
  public void questionerDownVotingQuestionIsNotAllowed() throws Exception {
    //Voting Exception is Catches Inside Class when questioner  DownVotes his Question
    thrown.expect(VotingException.class);
    thrown.expectMessage("You cannot vote for yourself");

    //Arrange: mQuestionUser is asking Question
    Question question = mQuestionUser.askQuestion("How do you convert int[] to ArrayList<Integer>");

    //Action: mQuestionUser is DownVoting Question
    mQuestionUser.downVote(question);
  }

  @Test
  public void answererUpVotingAnswerIsNotAllowed() throws Exception {
    //Voting Exception is Catches Inside Class when Answerer UpVotes his Answer
    thrown.expect(VotingException.class);
    thrown.expectMessage("You cannot vote for yourself");

    //Arrange: mQuestionUser is asking Question , mAnswerUser is answering Question
    Question question = mQuestionUser.askQuestion("Is is possible to access non-static members" +
        "without instance of class");
    Answer answer = mAnswerUser.answerQuestion(question,"No it is not possible");

    //Action : mAnswerUser UpVoting his answer
    mAnswerUser.upVote(answer);
  }

  @Test
  public void answererDownVotingAnswerIsNotAllowed() throws Exception {
    //Voting Exception is Catches Inside Class when Answerer DownVotes his Answer
    thrown.expect(VotingException.class);
    thrown.expectMessage("You cannot vote for yourself");

    //Arrange: mQuestionUser is asking Question, mAnswerUser is answering Question
    Question question = mQuestionUser.askQuestion("How do you convert int[] to ArrayList<Integer>");
    Answer answer = mAnswerUser.answerQuestion(question,"By using the static Array.asList Method");

    //Action: mAnswer DownVoting his answer
    mAnswerUser.downVote(answer);
  }

  @Test
  public void UnAuthorizedQuestionerAcceptingAnswerIsNotAllowed( )throws Exception {
    String message = String.format("Only %s can accept this answer as it is their question",
        mQuestionUser.getName());
    //Answer Acceptance Exception is catches when Other Board User is accepting Original Questioner Answer
    thrown.expect(AnswerAcceptanceException.class);
    thrown.expectMessage(message);


    //Arrange : mQuestioner asks Question, mAnswerUser is answering Question
    Question question = mQuestionUser.askQuestion("What is the difference between checked and " +
        "Unchecked Exception");
    Answer answer = mAnswerUser.answerQuestion(question,"Checked Exception must be caught while" +
        "UnChecked Exceptions do not need to be caught");

    //Action : mBoardUser(Unauthorized Questioner) accepting Answer
    mBoardUser.acceptAnswer(answer);

  }

  @Test
  public void questionerAcceptingAnswerToHisQuestionSetsAnswerIsAccepted() throws Exception {
    //Arrange : mQuestioner asks Question , mAnswerUser is answering Question
    Question question = mQuestionUser.askQuestion("Which Method is used to Sort a Collection by" +
        "Natural order or its elements");
    Answer answer = mAnswerUser.answerQuestion(question,"Collections.sort");

    //Action: mQuestionUser accepts answer
    mQuestionUser.acceptAnswer(answer);
    String message = String.format("%s accept answer to question",
        mQuestionUser.getName());

    //Assert : Verify
    assertTrue(message,answer.isAccepted());
  }
}