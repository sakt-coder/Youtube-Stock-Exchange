import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Channel } from '../models/channel';
import { User } from '../models/user';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  @Input() user: User;
  @Output() logoutEvent = new EventEmitter();

  constructor(private http:HttpClient) { }

  ngOnInit(): void {
    console.log(this.user);
  }
  
  sell(channel: Channel): void {
    console.log(channel);
  }

  logout():void {
    this.http.get(`${environment.url}/logout`)
      .subscribe(
        (data) => { 
          console.log(data);
          localStorage.removeItem('Token');
          this.logoutEvent.emit(null);
         }
      );
  }

}
