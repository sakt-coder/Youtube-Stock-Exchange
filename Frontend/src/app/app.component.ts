import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { User } from './models/user'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  title: string;
  currUser: User;
  recentTransactions: string[];
  leaderBoard: User[];

  constructor(private http: HttpClient) {
    this.title = 'Youtube Stock Exchange';
    this.currUser = null;
  }

  ngOnInit(): void {
    this.refresh();
  }

  refresh() {
    this.fetchRecentTransactions();
    this.fetchLeaderBoard();
    this.autoLogin();
  }

  login(user: User): void {
    this.currUser = user;
  }

  logout(): void {
    this.currUser = null;
  }

  autoLogin() {
    this.http.get(`${environment.url}/autoLogin`)
      .subscribe(
        (data:User) => { 
          this.login(data);
         }
      );
  }

  fetchRecentTransactions() {
    this.http.get(`${environment.url}/fetchRecentTransactions`)
      .subscribe(
        (data: string[]) => {
            this.recentTransactions = data;
         }
      );
  }

  fetchLeaderBoard() {
    this.http.get(`${environment.url}/fetchLeaderBoard`)
      .subscribe(
        (data: User[]) => {
          console.log(data);
            this.leaderBoard = data;
         }
      );
  }

}
