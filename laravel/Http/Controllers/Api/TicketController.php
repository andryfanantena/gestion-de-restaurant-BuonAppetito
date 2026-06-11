<?php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Order;
use App\Models\RestaurantTable;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class TicketController extends Controller
{
    /**
     * GET /api/orders/{id}/ticket
     * Génère un ticket HTML téléchargeable ou retourne son URL
     * Retourne : { url }
     *
     * NOTE : Nécessite barryvdh/laravel-dompdf en production.
     *        En démo, on génère un ticket HTML dans storage/public.
     */
    public function show(Request $request, int $id): JsonResponse
    {
        $order = Order::with(['items.dish', 'user'])->find($id);

        if (!$order) {
            return response()->json(['message' => 'Commande introuvable.'], 404);
        }

        if ($order->user_id !== $request->user()->id) {
            return response()->json(['message' => 'Non autorisé.'], 403);
        }

        $tableNumber = $order->restaurant_table_id
            ? RestaurantTable::find($order->restaurant_table_id)?->table_number
            : 'À emporter';

        // ── Génération HTML du ticket ─────────────────────────────────────────
        $html = $this->generateTicketHtml($order, $tableNumber);
        $filename = 'ticket_' . $order->order_number . '.html';

        Storage::disk('public')->put('tickets/' . $filename, $html);

        $url = url('storage/tickets/' . $filename);

        return response()->json(['url' => $url], 200);
    }

    private function generateTicketHtml(Order $order, ?string $tableNumber): string
    {
        $itemsHtml = '';
        foreach ($order->items as $item) {
            $subtotal   = number_format($item->dish->price * $item->quantity, 0, '.', ' ');
            $price      = number_format($item->dish->price, 0, '.', ' ');
            $itemsHtml .= "
            <tr>
                <td style='padding:6px 4px'>{$item->dish->name}</td>
                <td style='text-align:center'>{$item->quantity}</td>
                <td style='text-align:right'>{$price} Ar</td>
                <td style='text-align:right;font-weight:bold'>{$subtotal} Ar</td>
            </tr>";
        }

        $total = number_format($order->total_price, 0, '.', ' ');
        $date  = $order->created_at?->format('d/m/Y H:i') ?? '';

        return "<!DOCTYPE html>
<html lang='fr'>
<head>
  <meta charset='UTF-8'>
  <meta name='viewport' content='width=device-width, initial-scale=1'>
  <title>Ticket #{$order->order_number}</title>
  <style>
    body { font-family: 'Courier New', monospace; max-width: 380px; margin: 0 auto; padding: 20px; background: #fff; }
    .header { text-align: center; border-bottom: 2px dashed #333; padding-bottom: 16px; margin-bottom: 16px; }
    .logo { font-size: 24px; font-weight: bold; color: #2D6A4F; }
    .sub  { font-size: 12px; color: #666; }
    table { width: 100%; border-collapse: collapse; font-size: 13px; }
    th { background: #f0f0f0; padding: 8px 4px; text-align: left; font-size: 12px; }
    tr:nth-child(even) { background: #fafafa; }
    .total-row { border-top: 2px dashed #333; font-weight: bold; font-size: 16px; }
    .total-row td { padding: 12px 4px; }
    .footer { text-align: center; margin-top: 20px; font-size: 11px; color: #888; border-top: 1px dashed #ccc; padding-top: 12px; }
    .badge { display:inline-block; background:#2D6A4F; color:white; padding:4px 12px; border-radius:20px; font-size:12px; margin-top:6px; }
  </style>
</head>
<body>
  <div class='header'>
    <div class='logo'>🍽️ BuonAppetito</div>
    <div class='sub'>Votre expérience culinaire</div>
    <div class='sub' style='margin-top:8px'>
      <strong>#{$order->order_number}</strong><br>
      {$date}<br>
      Table : {$tableNumber}
    </div>
  </div>

  <table>
    <thead>
      <tr>
        <th>Plat</th><th style='text-align:center'>Qté</th>
        <th style='text-align:right'>Prix</th><th style='text-align:right'>Total</th>
      </tr>
    </thead>
    <tbody>
      {$itemsHtml}
      <tr class='total-row'>
        <td colspan='3'>TOTAL</td>
        <td style='text-align:right'>{$total} Ar</td>
      </tr>
    </tbody>
  </table>

  <div class='footer'>
    <p>Merci de votre visite !</p>
    <div class='badge'>Bon appétit 🌿</div>
    <p style='margin-top:12px'>Ce ticket fait foi de votre paiement.</p>
  </div>
</body>
</html>";
    }
}
