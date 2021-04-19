import { Component, Input, OnInit } from '@angular/core';
import { User } from '../models/user';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit {

  @Input() leaderBoard: User[];

  constructor() { }

  ngOnInit(): void {
  }

  parseInt(num: string) {
    return parseInt(num);
  }

}
