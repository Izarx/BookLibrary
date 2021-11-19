import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Author} from "../model/author";
import {Observable} from "rxjs";
import {Pageable} from "../model/pagable";
import {Page} from "../model/page";
import {PageConstructor} from "../model/page-constructor";

const baseUrl = 'http://localhost:8080/api/authors';

@Injectable({
  providedIn: 'root'
})
export class AuthorService {

  constructor(private httpClient: HttpClient) { }

  public createAuthor(author: Author) : Observable<Author> {
    return this.httpClient.post<Author>(baseUrl + `/create`, author);
  }

  public deleteAuthor(authorId : number) : Observable<void> {
    return this.httpClient.delete<void>(baseUrl + `/delete/${authorId}`);
  }

  public getPage(pageConstructor: PageConstructor): Observable<Page<Author>> {
    return this.httpClient.post<Page<Author>>(baseUrl, pageConstructor);
  }

  public updateAuthor(author: Author) : Observable<any> {
        return this.httpClient.put(baseUrl + `/update`, author);
  }
}
