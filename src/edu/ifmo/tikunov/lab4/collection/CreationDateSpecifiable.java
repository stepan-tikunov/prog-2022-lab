package edu.ifmo.tikunov.lab4.collection;

import java.time.LocalDateTime;

/**
 * Any type that implements this interface has creation date.
 */
public interface CreationDateSpecifiable {
	void setCreationDate(LocalDateTime newDate);

	LocalDateTime getCreationDate();
}
