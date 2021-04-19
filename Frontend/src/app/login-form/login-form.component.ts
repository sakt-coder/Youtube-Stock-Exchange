import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { User } from '../models/user';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})

export class LoginFormComponent implements OnInit {

  username: string;
  password: string;
  reply: string;
  @Output() loginEvent = new EventEmitter<User>();

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  login(){
    let body = new HttpParams();
    body = body.set('username', this.username);
    body = body.set('password', this.password);
    this.http.post(`${environment.url}/login`,body)
      .subscribe(
        (data:User) => { 
          this.loginEvent.emit(data);
         }
      );
  }

  register() {
    let body = new HttpParams();
    body = body.set('username', this.username);
    body = body.set('password', this.password);
    this.http.post(`${environment.url}/register`,body)
      .subscribe(
        (data:User) => { 
          this.loginEvent.emit(data);
         }
      );
  }

}
