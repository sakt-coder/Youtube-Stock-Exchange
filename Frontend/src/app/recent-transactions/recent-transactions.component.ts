import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-recent-transactions',
  templateUrl: './recent-transactions.component.html',
  styleUrls: ['./recent-transactions.component.css']
})
export class RecentTransactionsComponent implements OnInit {

  @Input() recentTransactions: string[];

  constructor() { }

  ngOnInit(): void {
  }

}
