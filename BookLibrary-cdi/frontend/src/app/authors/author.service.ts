import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Author} from "../model/author";
import {Observable} from "rxjs";
import {ResponseData} from "../model/response-data";
import {RequestOptions} from "../model/request-options";

const baseUrl = 'http://localhost:8080/api/authors';

@Injectable({
    providedIn: 'root'
})
export class AuthorService {

    constructor(private httpClient: HttpClient) {
    }

    public createAuthor(author: Author): Observable<Author> {
        return this.httpClient.post<Author>(baseUrl + `/create`, author);
    }

    public deleteAuthor(authorId: number): Observable<void> {
        return this.httpClient.delete<void>(baseUrl + `/delete/${authorId}`);
    }

    public getPage(appRequestPage: RequestOptions): Observable<ResponseData<Author>> {
        return this.httpClient.post<ResponseData<Author>>(baseUrl, appRequestPage);
    }

    public updateAuthor(author: Author): Observable<any> {
        return this.httpClient.put(baseUrl + `/update`, author);
    }

    getAuthorById(authorId: number): Observable<Author> {
        return this.httpClient.get<Author>(baseUrl + `/${authorId}`);
    }
}
