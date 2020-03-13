﻿using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using PolyPaint.Modeles;
using PolyPaint.Utilitaires;
using Svg;
using System.Windows.Markup;
using System.Xml.Linq;
using System;
using Quobject.SocketIoClientDotNet.Client;
using Newtonsoft.Json;

namespace PolyPaint.VueModeles
{

    /// <summary>
    /// Sert d'intermédiaire entre la vue et le modèle.
    /// Expose des commandes et propriétés connectées au modèle aux des éléments de la vue peuvent se lier.
    /// Reçoit des avis de changement du modèle et envoie des avis de changements à la vue.
    /// </summary>
    class DrawingWindowViewModel : INotifyPropertyChanged, IPageViewModel
    {
        public event PropertyChangedEventHandler PropertyChanged;
        private Editeur editeur = new Editeur();
        private SvgDocument newImage = new SvgDocument();
        private Socket socket = IO.Socket("http://10.200.8.135:5050");
        private Editeur temp = new Editeur();

        // Ensemble d'attributs qui définissent l'apparence d'un trait.
        public DrawingAttributes AttributsDessin { get; set; } = new DrawingAttributes();

        private ICommand _goToGameModeMenu;

        public ICommand GoToGameModeMenu
        {
            get
            {
                return _goToGameModeMenu ?? (_goToGameModeMenu = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameModeMenu", "");
                }));
            }
        }

        public ICommand ButtonCommand { get; set; }


        public string OutilSelectionne
        {
            get { return editeur.OutilSelectionne; }
            set { ProprieteModifiee(); }
        }

        public string CouleurSelectionnee
        {
            get { return editeur.CouleurSelectionnee; }
            set { editeur.CouleurSelectionnee = value; }
        }

        public string PointeSelectionnee
        {
            get { return editeur.PointeSelectionnee; }
            set { ProprieteModifiee(); }
        }

        public int TailleTrait
        {
            get { return editeur.TailleTrait; }
            set { editeur.TailleTrait = value; }
        }

        public StrokeCollection Traits { get; set; }

        // Commandes sur lesquels la vue pourra se connecter.
        public RelayCommand<string> ChoisirPointe { get; set; }
        public RelayCommand<string> ChoisirOutil { get; set; }

        /// <summary>
        /// Constructeur de VueModele
        /// On récupère certaines données initiales du modèle et on construit les commandes
        /// sur lesquelles la vue se connectera.
        /// </summary>
        public DrawingWindowViewModel()
        {
            ButtonCommand = new RelayCommand(o => ConvertDrawingToSVG("ToSVG"));
            // On écoute pour des changements sur le modèle. Lorsqu'il y en a, EditeurProprieteModifiee est appelée.
            editeur.PropertyChanged += new PropertyChangedEventHandler(EditeurProprieteModifiee);

            // On initialise les attributs de dessin avec les valeurs de départ du modèle.
            AttributsDessin = new DrawingAttributes();
            AttributsDessin.Color = (Color)ColorConverter.ConvertFromString(editeur.CouleurSelectionnee);
            AjusterPointe();

            Traits = editeur.traits;
            temp = editeur;

            // Pour les commandes suivantes, il est toujours possible des les activer.
            // Donc, aucune vérification de type Peut"Action" à faire.
            ChoisirPointe = new RelayCommand<string>(editeur.ChoisirPointe);
            ChoisirOutil = new RelayCommand<string>(editeur.ChoisirOutil);

            socket.On("drawingAttributes", drawingAttributes => {
                Console.WriteLine("drawingAttributes: " + drawingAttributes.ToString());
                AttributsDessin = JsonConvert.DeserializeObject<DrawingAttributes>(drawingAttributes.ToString());
                Console.WriteLine("Taille: " + AttributsDessin.Width);
                temp.TailleTrait = (int) AttributsDessin.Width;
                temp.PointeSelectionnee = (AttributsDessin.StylusTip.Equals(StylusTip.Ellipse)) ? "ronde" : "carre";
                AjusterPointe();
            });
        }

        private void ConvertDrawingToSVG(object sender)
        {

            var svg = new SvgDocument();
            var colorServer = new SvgColourServer(System.Drawing.Color.Black);

            var group = new SvgGroup { Fill = colorServer, Stroke = colorServer };
            svg.Children.Add(group);

            foreach (var stroke in Traits)
            {
                var geometry = stroke.GetGeometry(stroke.DrawingAttributes).GetOutlinedPath‌​Geometry();

                var s = XamlWriter.Save(geometry);

                if (!string.IsNullOrEmpty(s))
                {
                    var element = XElement.Parse(s);

                    var data = element.Attribute("Figures")?.Value;

                    if (!string.IsNullOrEmpty(data))
                    {
                        group.Children.Add(new SvgPath
                        {
                            PathData = SvgPathBuilder.Parse(data),
                            Fill = colorServer,
                            Stroke = colorServer
                        });
                    }
                }
            }
            Console.WriteLine(group.GetXML());
            //newImage = svg;
        }
    

        /// <summary>
        /// Appelee lorsqu'une propriété de VueModele est modifiée.
        /// Un évènement indiquant qu'une propriété a été modifiée est alors émis à partir de VueModèle.
        /// L'évènement qui contient le nom de la propriété modifiée sera attrapé par la vue qui pourra
        /// alors mettre à jour les composants concernés.
        /// </summary>
        /// <param name="propertyName">Nom de la propriété modifiée.</param>
        protected virtual void ProprieteModifiee([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        /// <summary>
        /// Traite les évènements de modifications de propriétés qui ont été lancés à partir
        /// du modèle.
        /// </summary>
        /// <param name="sender">L'émetteur de l'évènement (le modèle)</param>
        /// <param name="e">Les paramètres de l'évènement. PropertyName est celui qui nous intéresse. 
        /// Il indique quelle propriété a été modifiée dans le modèle.</param>
        private void EditeurProprieteModifiee(object sender, PropertyChangedEventArgs e)
        {
            Console.WriteLine("Debut: " + AttributsDessin.Color.ToString());
            if (e.PropertyName == "CouleurSelectionnee")
            {
                Console.WriteLine("Change color");
                AttributsDessin.Color = (Color)ColorConverter.ConvertFromString(editeur.CouleurSelectionnee);
            }
            else if (e.PropertyName == "OutilSelectionne")
            {
                Console.WriteLine("Change outil");
                OutilSelectionne = editeur.OutilSelectionne;
            }
            else if (e.PropertyName == "PointeSelectionnee")
            {
                Console.WriteLine("Change pointe");
                PointeSelectionnee = editeur.PointeSelectionnee;
                AjusterPointe();
            }
            else // e.PropertyName == "TailleTrait"
            {
                Console.WriteLine("Change taille");
                AjusterPointe();
            }
            Console.WriteLine("Fin: " + AttributsDessin.Color.ToString());
            //send();
            socket.Emit("drawingAttributes", "General ", JsonConvert.SerializeObject(AttributsDessin));
        }

        /// <summary>
        /// C'est ici qu'est défini la forme de la pointe, mais aussi sa taille (TailleTrait).
        /// Pourquoi deux caractéristiques se retrouvent définies dans une même méthode? Parce que pour créer une pointe 
        /// horizontale ou verticale, on utilise une pointe carrée et on joue avec les tailles pour avoir l'effet désiré.
        /// </summary>
        private void AjusterPointe()
        {
            Console.WriteLine("AjusterPointe");
            AttributsDessin.StylusTip = (editeur.PointeSelectionnee == "ronde") ? StylusTip.Ellipse : StylusTip.Rectangle;
            AttributsDessin.Width = editeur.TailleTrait;
            AttributsDessin.Height = editeur.TailleTrait;
        }

        private void send()
        {
            socket.Emit("drawingAttributes", "General ", JsonConvert.SerializeObject(AttributsDessin));
        }

    }

}
