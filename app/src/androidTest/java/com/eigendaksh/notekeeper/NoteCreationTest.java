package com.eigendaksh.notekeeper;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.junit.Assert.*;

import static android.support.test.espresso.Espresso.pressBack;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    static DataManager sDataManager;

    @BeforeClass
    public static void setUpClass() throws Exception {
        sDataManager = DataManager.getInstance();
    }

    @Rule
    public ActivityTestRule<NoteListActivity> mNoteListActivityRule
            = new ActivityTestRule<>(NoteListActivity.class);

    @Test
    public void createNewNote() throws Exception {
        final CourseInfo courseInfo = sDataManager.getCourse("java_lang");
        final String noteTitle = "Title of test note";
        final String noteText = "Body of test note";

        // Click on FAB
        onView(withId(R.id.fab)).perform(click());

        // Click and select from spinner
        onView(withId(R.id.spinner_courses)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class), equalTo(courseInfo))).perform(click());
        onView(withId(R.id.spinner_courses))
                .check(matches(withSpinnerText(containsString(courseInfo.getTitle()))));

        // Add text to title and body
        onView(withId(R.id.text_note_title)).perform(typeText(noteTitle));
        onView(withId(R.id.text_note_text)).perform(typeText(noteText), closeSoftKeyboard());

        // Edit text contains the text we typed
        onView(withId(R.id.text_note_text)).check(matches(withText(containsString(noteText))));

        pressBack();

        // Check note is added
        int nodeIndex = sDataManager.getNotes().size() - 1;
        NoteInfo noteInfo = sDataManager.getNotes().get(nodeIndex);

        assertEquals(courseInfo, noteInfo.getCourse());
        assertEquals(noteTitle, noteInfo.getTitle());
        assertEquals(noteText, noteInfo.getText());
    }

}