package edu.coloradomesa.mytutor;

import static edu.coloradomesa.mytutor.Util.eq;

/**
 * Created by wmacevoy on 9/13/17.
 */

public class Course {
    long mId;
    String mDepartment;
    long mNumber;

    Course(long id, String department, long number) {
        mId = id;
        mDepartment = department;
        mNumber = number;
    }

    Course(String department, long number) {
        mId = -1;
        mDepartment = department;
        mNumber = number;
    }

    @Override public boolean equals(Object object) {
        if (object != null && object instanceof Course) {
            Course course = (Course) object;
            return (mId == course.mId || mId == -1 || course.mId == -1) && eq(mDepartment,course.mDepartment) && mNumber == course.mNumber;
        }
        return false;
    }

    @Override public String toString() {
        return "Course(" + mId + ",\"" + mDepartment + "\"," + mNumber + ")";
    }

}
