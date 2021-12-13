package com.softserveinc.booklibrary.backend.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortableColumn {

	private String name;
	private SortingDirection direction;

}
