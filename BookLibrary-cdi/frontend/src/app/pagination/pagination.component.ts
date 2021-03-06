import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RequestOptions} from "../model/request-options";

@Component({
    selector: 'app-pagination',
    templateUrl: './pagination.component.html',
    styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent<T> implements OnInit {

    @Input() requestOptions: RequestOptions<T>;
    @Output() nextPageEvent = new EventEmitter();
    @Output() previousPageEvent = new EventEmitter();
    @Output() pageSizeEvent: EventEmitter<number> = new EventEmitter<number>();
    @Output() pageNumberEvent: EventEmitter<number> = new EventEmitter<number>();

    constructor() {
    }

    ngOnInit(): void {
    }

    nextPage(): void {
        this.nextPageEvent.emit(null);
    }

    previousPage(): void {
        this.previousPageEvent.emit(null);
    }

    updatePageNumber(pageNumber: number): void {
        this.pageNumberEvent.emit(pageNumber);
    }

    updatePageSize(pageSize: number): void {
        this.pageSizeEvent.emit(pageSize);
    }

}
