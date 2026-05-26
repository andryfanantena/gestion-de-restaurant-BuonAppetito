<?php

use App\Livewire\KitchenDashboard;
use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return view('welcome');
});

Route::get('/kitchen', KitchenDashboard::class);
