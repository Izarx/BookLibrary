package com.softserveinc.booklibrary.entity;

import java.io.Serializable;

public interface MyAppEntity<K extends Serializable> extends Serializable {

	K getEntityId();

}
