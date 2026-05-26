<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;

class RoleMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure(\Illuminate\Http\Request): (\Symfony\Component\HttpFoundation\Response)  $next
     * @param  string  ...$roles
     * @return \Symfony\Component\HttpFoundation\Response
     */
    public function handle(Request $request, Closure $next, ...$roles): Response
    {
        // 1. Vérifier si l'utilisateur est authentifié via son Token
        if (!$request->user()) {
            return response()->json([
                'message' => 'Non authentifié. Token manquant ou invalide.'
            ], 401);
        }

        // 2. Vérifier si le rôle de l'utilisateur est dans la liste des rôles autorisés
        if (!in_array($request->user()->role, $roles)) {
            return response()->json([
                'message' => 'Accès interdit. Privilèges insuffisants pour le rôle : ' . $request->user()->role
            ], 403);
        }

        return $next($request);
    }
}