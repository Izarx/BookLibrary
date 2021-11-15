import {Component, OnInit} from '@angular/core';
import {Page} from "../../model/page";
import {PaginationService} from "../../pagination/pagination.service";
import {Book} from "../../model/book";
import {BookService} from "../book.service";

@Component({
  selector: 'app-books-pagination-table',
  templateUrl: './books-pagination-table.component.html',
  styleUrls: ['./books-pagination-table.component.scss']
})
export class BooksPaginationTableComponent implements OnInit {


  page: Page<Book> = new Page()
  book: Book = new Book(null, '', 0, 0, '', 0.0)

  constructor(
      private bookService: BookService,
      private paginationService: PaginationService
  ) { }

  ngOnInit(): void {
    this.getData()
  }

  private getData(): void {
    this.bookService.getPage(this.page.pageable)
        .subscribe(page => {
              this.page = page
            },
            error => {
              console.log(error)
            });
  }

  deleteBook(bookId : number) : void {
    this.bookService.deleteBook(bookId).subscribe(
        () => {
          this.getData()
        },
        error => {
          console.log(error)
        })
  }
  public getNextPage(): void {
    this.page.pageable = this.paginationService.getNextPage(this.page);
    this.getData();
  }

  public getPreviousPage(): void {
    this.page.pageable = this.paginationService.getPreviousPage(this.page);
    this.getData();
  }

  public getPageInNewSize(pageSize: number): void {
    this.page.pageable = this.paginationService.getPageInNewSize(this.page, pageSize);
    this.getData();
  }

  public getPageNewNumber(pageNumber: number): void {
    this.page.pageable = this.paginationService.getPageNewNumber(this.page, pageNumber);
    this.getData();
  }

  setBook(book: Book) {
    this.book = book
  }

  getBook() : Book {
    return this.book;
  }
}
