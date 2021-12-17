import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Author} from "../../model/author";
import {AuthorService} from "../../services/author.service";

@Component({
    selector: 'app-update-author',
    templateUrl: './update-author.component.html',
    styleUrls: ['./update-author.component.scss']
})
export class UpdateAuthorComponent implements OnInit {

    form: FormGroup = new FormGroup({});
    @Input() author: Author;
    @Output() initParentPage: EventEmitter<any> = new EventEmitter<any>();

    constructor(private authorService: AuthorService) {
    }

    ngOnInit(): void {
        this.form = new FormGroup({
            firstName: new FormControl('', [
                Validators.required]),
            lastName: new FormControl('', [])
        });
    }

    updateAuthor(): void {
        this.authorService.updateAuthor(this.author).subscribe(
            author => {
                this.author = author;
                this.initParentPage.emit(null);
            },
            error => {
                console.log(error);
            });
    }

    submit(): void {
        if (this.form.valid) {
            const formData = {...this.form.value};
            let firstName = formData.firstName;
            if (firstName !== null && firstName !== undefined) {
                this.author.firstName = firstName.trim();
            }
            let lastName = formData.lastName;
            if (lastName !== null && lastName !== undefined) {
                this.author.lastName = lastName.trim();
            }
            this.updateAuthor();
            this.form.reset();
            document.getElementById('updateAuthorModalCloseButton').click();
        }
    }

    cancel(): void {
        this.form.reset();
    }
}
