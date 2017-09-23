/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.delaroy.employeedirectory.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;


public final class EmployeeContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private EmployeeContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.delaroy.employeedirectory";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_EMPLOYEES = "employees-path";

    /**
     * Inner class that defines constant values for the employees database table.
     * Each entry in the table represents a single employee.
     */
    public static final class EmployeeEntry implements BaseColumns {

        /** The content URI to access the employee data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EMPLOYEES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of employees.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single employee.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEES;

        /** Name of database table for employees */
        public final static String TABLE_NAME = "employees";

      /**
         * Unique ID number for the employee (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the employee.
         *
         * Type: TEXT
         */
        public final static String COLUMN_FIRSTNAME = "firstname";
        public final static String COLUMN_LASTNAME = "lastname";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_DEPARTMENT = "department";
        public final static String COLUMN_CITY = "city";
        public final static String COLUMN_PHONE = "phone";
        public final static String COLUMN_IMAGE = "image";
        public final static String COLUMN_EMAIL = "email";

        /**
         * Gender of the employee.
         *
         * The only possible values are {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_EMPLOYEE_GENDER = "gender";

        /**
         * Possible values for the gender of the employee.
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        /**
         * Returns whether or not the given gender is {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         */
        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }
    }

}

