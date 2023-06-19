package com.tiokolane.jur_gi_auth.service;

import com.tiokolane.jur_gi_auth.model.EmailDetails;

// Importing required classes
 
// Interface
public interface EmailService {
 
    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);
 
    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}