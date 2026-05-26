<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        // Validation des paramètres de la requête
        $fields = $request->validate([
            'name' => 'required|string|max:255',
            'email' => 'required|string|email|max:255|unique:users,email',
            'password' => 'required|string|min:6',
            'role' => 'nullable|string|in:client,serveur,cuisinier,admin'
        ]);

        // Création de l'utilisateur
        $user = User::create([
            'name' => $fields['name'],
            'email' => $fields['email'],
            'password' => Hash::make($fields['password']),
            'role' => $fields['role'] ?? 'client' // 'client' par défaut si omis
        ]);

        // Création du jeton d'authentification Sanctum
        $token = $user->createToken('buonappetitotoken')->plainTextToken;

        return response()->json([
            'user' => $user,
            'token' => $token
        ], 201);
    }

    public function login(Request $request)
    {
        $fields = $request->validate([
            'email' => 'required|string|email',
            'password' => 'required|string'
        ]);

        // Recherche de l'utilisateur par e-mail
        $user = User::where('email', $fields['email'])->first();

        // Vérification de l'existence de l'utilisateur et du mot de passe haché
        if (!$user || !Hash::check($fields['password'], $user->password)) {
            return response()->json([
                'message' => 'Identifiants incorrects'
            ], 401);
        }

        // Génération d'un nouveau jeton d'accès suite à la connexion réussie
        $token = $user->createToken('buonappetitotoken')->plainTextToken;

        return response()->json([
            'user' => $user,
            'token' => $token
        ], 200);
    }

    public function logout(Request $request)
    {
        // Révocation (suppression) du token utilisé pour la requête actuelle
        $request->user()->currentAccessToken()->delete();

        return response()->json([
            'message' => 'Déconnexion réussie. Jeton révoqué.'
        ], 200);
    }
}