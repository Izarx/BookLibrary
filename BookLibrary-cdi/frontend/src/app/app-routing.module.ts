import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {HomeComponent} from "./home/home.component";
import {AuthorsComponent} from "./authors/authors.component";
import {BooksComponent} from "./books/books.component";
import {ReviewsComponent} from "./reviews/reviews.component";

const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'authors', component: AuthorsComponent},
    {path: 'books', component: BooksComponent},
    {path: 'reviews', component: ReviewsComponent}
]

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {

}